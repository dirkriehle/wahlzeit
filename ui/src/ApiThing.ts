interface Photo {
  id: number;
  userId: number;
  path: string;
  tags: string[];
  width: number;
  height: number;
  praise: number;
}

interface User {
  id: number;
  name: string;
  email: string;
}

class ApiThing {
  auth: string | null;
  user: User | null;

  constructor() {
    this.auth = sessionStorage.getItem("auth");
    const userjson = sessionStorage.getItem("user");
    if (userjson) {
      this.user = JSON.parse(userjson);
    } else {
      this.user = null;
    }
  }

  get isLoggedIn() {
    return this.auth != null;
  }

  async request(endpoint: string, method: string, body: any = null) {
    const init: RequestInit = {};
    init.method = method;
    if (body) {
      init.body = body;
    }
    init.headers = { "Content-Type": "application/json" };
    if (this.auth) {
      init.headers["Authorization"] = `Basic ${this.auth}`;
    }
    const response = await fetch(`http://localhost:8080/api/${endpoint}`, init);
    if (response.ok) {
      return await response.json();
    } else {
      throw (await response.json())["msg"];
    }
  }

  async login(username: string, password: string) {
    return this.request(
      "user/login",
      "POST",
      JSON.stringify({
        email: username,
        password: password
      })
    ).then((user: User) => {
      console.log(user);
      this.auth = btoa(`${user.name}:${password}`);
      this.user = user;
      sessionStorage.setItem("auth", this.auth);
      sessionStorage.setItem("user", JSON.stringify(user));
    });
  }

  logout() {
    this.auth = null;
    this.user = null;
    sessionStorage.removeItem("auth");
    sessionStorage.removeItem("user");
  }

  async signup(name: string, email: string, password: string): Promise<User> {
    return this.request(
      "user",
      "POST",
      JSON.stringify({
        name: name,
        email: email,
        password: password
      })
    );
  }

  async getPhoto(id: string): Promise<Photo> {
    return this.request(`photo/${id}`, "GET");
  }

  async uploadPhoto(file: File, tags: string[] = []): Promise<Photo> {
    let endpoint = "photo";
    if (tags.length > 0) {
      endpoint += "?tags=";
      endpoint += tags.join("&tags=");
    }
    return this.request(endpoint, "POST", file);
  }

  async removePhoto(id: number): Promise<Photo> {
    return this.request(`photo/${id}`, "DELETE");
  }

  async praisePhoto(id: number, value: number): Promise<Photo> {
    return this.request(`photo/${id}/praise`, "POST", value);
  }

  async listPhotos(userid: number | null = null, tags: string[] = []) {
    const params = new URLSearchParams();
    if (userid != null) {
      params.append("user", userid.toString());
    }
    for (const tag of tags) {
      params.append("tags", tag);
    }
    let endpoint = "photo";
    if (params.toString().length > 0) {
      endpoint += "?";
      endpoint += params.toString();
    }
    return this.request(endpoint, "GET");
  }

  async getUsers() {
    return this.request("user", "GET");
  }

  async getUser(id: number): Promise<User> {
    return this.request(`user/${id}`, "GET");
  }

  async deleteUser() {
    return this.request("user", "DELETE");
  }
}

export { ApiThing, Photo, User };

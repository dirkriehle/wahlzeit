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
  id2: number;
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

  async request(
    endpoint: string,
    method: string,
    body: any = null,
    auth = false
  ) {
    const init: RequestInit = {};
    init.method = method;
    if (body) {
      init.body = body;
    }
    init.headers = { "Content-Type": "application/json" };
    if (auth) {
      if (!this.auth) {
        throw "Not logged in";
      }
      init.headers["Authorization"] = `Basic ${this.auth}`;
    }
    console.log(init);
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

  async getPhoto(id: string): Promise<Photo> {
    return this.request(`photo/${id}`, "GET");
  }

  async uploadPhoto(file: File): Promise<Photo> {
    return this.request("photo", "POST", file, true);
  }
}

export { ApiThing, Photo };

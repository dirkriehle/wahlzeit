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

class WahlzeitApi {
  auth: string | null;
  user: User | null;

  constructor() {
    this.auth = sessionStorage.getItem('auth');
    const userJson = sessionStorage.getItem('user');
    if (userJson) {
      this.user = JSON.parse(userJson) as User;
    } else {
      this.user = null;
    }
  }

  get isLoggedIn(): boolean {
    return this.auth != null;
  }

  async request(
    endpoint: string,
    method: string,
    body?: unknown,
  ): Promise<unknown> {
    const init: RequestInit = {};
    init.method = method;
    if (body !== undefined) {
      init.body = body as BodyInit;
    }
    init.headers = { 'Content-Type': 'application/json' };
    if (this.auth) {
      init.headers.Authorization = `Basic ${this.auth}`;
    }
    const response = await fetch(`/api/${endpoint}`, init);
    if (!response.ok) {
      const responseJson = (await response.json()) as { msg?: string };
      const errorMsg = responseJson.msg ?? 'Unknown Error';
      throw new Error(errorMsg);
    }
    return (await response.json()) as unknown;
  }

  async login(username: string, password: string): Promise<void> {
    const user = (await this.request(
      'user/login',
      'POST',
      JSON.stringify({
        email: username,
        password: password,
      }),
    )) as User;
    this.auth = btoa(`${user.name}:${password}`);
    this.user = user;
    sessionStorage.setItem('auth', this.auth);
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  logout(): void {
    this.auth = null;
    this.user = null;
    sessionStorage.removeItem('auth');
    sessionStorage.removeItem('user');
  }

  async signup(name: string, email: string, password: string): Promise<User> {
    return (await this.request(
      'user',
      'POST',
      JSON.stringify({
        name: name,
        email: email,
        password: password,
      }),
    )) as User;
  }

  async getPhoto(id: string): Promise<Photo> {
    return (await this.request(`photo/${id}`, 'GET')) as Photo;
  }

  async uploadPhoto(file: File, tags: string[] = []): Promise<Photo> {
    let endpoint = 'photo';
    if (tags.length > 0) {
      endpoint += '?tags=';
      endpoint += tags.join('&tags=');
    }
    return (await this.request(endpoint, 'POST', file)) as Photo;
  }

  async removePhoto(id: number): Promise<Photo> {
    return (await this.request(`photo/${id}`, 'DELETE')) as Photo;
  }

  async praisePhoto(id: number, value: number): Promise<Photo> {
    return (await this.request(`photo/${id}/praise`, 'POST', value)) as Photo;
  }

  async listPhotos(
    userid: number | null = null,
    tags: string[] = [],
  ): Promise<Photo[]> {
    const params = new URLSearchParams();
    if (userid != null) {
      params.append('user', userid.toString());
    }
    for (const tag of tags) {
      params.append('tags', tag);
    }
    let endpoint = 'photo';
    if (params.toString().length > 0) {
      endpoint += '?';
      endpoint += params.toString();
    }
    return (await this.request(endpoint, 'GET')) as Photo[];
  }

  async getUsers(): Promise<User[]> {
    return (await this.request('user', 'GET')) as User[];
  }

  async getUser(id: number): Promise<User> {
    return (await this.request(`user/${id}`, 'GET')) as User;
  }

  async deleteUser(): Promise<User> {
    return (await this.request('user', 'DELETE')) as User;
  }

  reportPhoto(
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    photo: Photo,
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    reason: string,
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    explanation: string,
  ): Promise<void> {
    throw new Error('report photo not implemented by the API');
  }
}

const wahlzeitApi = new WahlzeitApi();

export { WahlzeitApi, wahlzeitApi, Photo, User };

interface AuthUser {
  id: number;
  name: string;
  email: string;
}

class WahlzeitApi {
  authToken?: string;
  currentUser?: AuthUser;

  constructor() {
    this.authToken = sessionStorage.getItem('auth') || undefined;
    const userJson = sessionStorage.getItem('user');
    if (userJson) {
      this.currentUser = JSON.parse(userJson) as AuthUser;
    } else {
      this.currentUser = undefined;
    }
  }

  get isLoggedIn(): boolean {
    return this.authToken !== undefined;
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
    if (this.authToken) {
      init.headers.Authorization = `Basic ${this.authToken}`;
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
    )) as AuthUser;
    this.authToken = btoa(`${user.name}:${password}`);
    this.currentUser = user;
    sessionStorage.setItem('auth', this.authToken);
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  logout(): void {
    this.authToken = undefined;
    this.currentUser = undefined;
    sessionStorage.removeItem('auth');
    sessionStorage.removeItem('user');
  }

  async signup(
    name: string,
    email: string,
    password: string,
  ): Promise<AuthUser> {
    return (await this.request(
      'user',
      'POST',
      JSON.stringify({
        name: name,
        email: email,
        password: password,
      }),
    )) as AuthUser;
  }
}

const wahlzeitApi = new WahlzeitApi();

export { WahlzeitApi, wahlzeitApi };

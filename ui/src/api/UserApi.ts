import { WahlzeitApi, wahlzeitApi } from './WahlzeitApi';

interface User {
  id: number;
  name: string;
  email: string;
}

class UserApi {
  constructor(private wahlzeitApi: WahlzeitApi) {}

  async getUsers(): Promise<User[]> {
    return (await this.wahlzeitApi.request('user', 'GET')) as User[];
  }

  async getUser(id: number): Promise<User> {
    return (await this.wahlzeitApi.request(`user/${id}`, 'GET')) as User;
  }

  async deleteUser(): Promise<User> {
    return (await this.wahlzeitApi.request('user', 'DELETE')) as User;
  }
}

const userApi = new UserApi(wahlzeitApi);

export { userApi, UserApi, User };

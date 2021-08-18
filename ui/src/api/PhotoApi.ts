import { WahlzeitApi, wahlzeitApi } from './WahlzeitApi';

interface Photo {
  id: number;
  userId: number;
  path: string;
  tags: string[];
  width: number;
  height: number;
  praise: number;
}

class PhotoApi {
  constructor(private wahlzeitApi: WahlzeitApi) {}

  async getPhoto(id: string): Promise<Photo> {
    return (await this.wahlzeitApi.request(`photo/${id}`, 'GET')) as Photo;
  }

  async uploadPhoto(file: File, tags: string[] = []): Promise<Photo> {
    let endpoint = 'photo';
    if (tags.length > 0) {
      endpoint += '?tags=';
      endpoint += tags.join('&tags=');
    }
    return (await this.wahlzeitApi.request(endpoint, 'POST', file)) as Photo;
  }

  async removePhoto(id: number): Promise<Photo> {
    return (await this.wahlzeitApi.request(`photo/${id}`, 'DELETE')) as Photo;
  }

  async praisePhoto(id: number, value: number): Promise<Photo> {
    return (await this.wahlzeitApi.request(
      `photo/${id}/praise`,
      'POST',
      value,
    )) as Photo;
  }

  async listPhotos(userid?: number, tags: string[] = []): Promise<Photo[]> {
    const params = new URLSearchParams();
    if (userid !== undefined) {
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
    return (await this.wahlzeitApi.request(endpoint, 'GET')) as Photo[];
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

const photoApi = new PhotoApi(wahlzeitApi);

export { photoApi, PhotoApi, Photo };

import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';

@inject(HttpClient)
export class IgService {
  token = localStorage.getItem('token');
  constructor(http) {
    http.configure(config => {
      config
        .withBaseUrl('https://api.instagram.com/v1/');
    });

    this.http = http;
  }

  recent() {
    return this.http.jsonp(`users/self/media/recent/?access_token=${this.token}`, 'callback');
  }

  me() {
    return this.http.jsonp(`users/self/?access_token=${this.token}`, 'callback');
  }

}

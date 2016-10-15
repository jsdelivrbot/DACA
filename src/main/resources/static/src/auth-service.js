import Auth0 from 'auth0-js';

export class AuthService {
  constructor() {
    this.auth0 = new Auth0({
      domain:       'chris92.auth0.com',
      clientID:     'n99H6yUSrrX44lHrVeqpvlRzgQEBP8gV',
      callbackURL:  'http://localhost:8080/#/',
      callbackOnLocationHash: true
    });
  }

  sigin() {
    const _this = this;
    this.auth0.login({
      connection: 'instagram',
      popup: true
    },
    function(err, profile) {
      if (err) {
        alert("something went wrong: " + err.message);
        return;
      }
      _this.auth0.getProfile(profile.idToken, function (err, profile) {
        if(err) {
          alert(err);
          return;
        }
        localStorage.setItem('token', profile.identities[0].access_token);
      });
    });
  }

  signout() {
    localStorage.removeItem('token');
  }

}

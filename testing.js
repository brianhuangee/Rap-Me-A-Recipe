var SpotifyWebApi = require('spotify-web-api-node');
var XMLHttpRequest = require('xmlhttprequest').XMLHttpRequest;
var sleep = require('then-sleep');

module.exports = {
  player: function(songs) {
    var access_token;
    var device_id;

    songs = JSON.parse(decodeURI(songs.split("+").join("")));

    access_token = "BQBmQ3DiSysJuEvYdVfcwR5zCkP9IAnKNMBW3wBlalfYP-e9VTWlSH-u7rPYA8xgb6U1xM3OQQQQuSYrWMZVAgUsQYk38kE3sPDU-WuBu28oTkYc4nqA0PQS8_pd_y65kUSo0hCiYq_pv_xprkWJhuOI8v3GTRJNiaQj514wJ6QilWe5CShIfqzRQUl149C6bvFsNrwt0LlZK37aDC6oWRm2PGQAYqSPU_-xXgKJLfHjbOU0f2CAbvkm0FkfG_8izzVMxvYjhOqg1EFPshO8aXh3vHZ1SWkdsBjAP2IXvQyMwQvUoDrYbOZjFsFlGIuOnQ";
    device_id = "028544928f36409f9fea78fc410396306b81dc08";

    var spotifyApi = new SpotifyWebApi();

    spotifyApi.setAccessToken(access_token);

      (function looper(ind) {
        var element = songs[ind];
        console.log(element.id);
        setTimeout(setSong(element.id), 1200);
        seekSong(element.start);
        if (ind < songs.length) setTimeout(function() {
          looper(++ind);
        }, 2000);
      })(0);

    spotifyApi.pause({"device_id": device_id});

    function setSong(id) {
      var xmlhttp = new XMLHttpRequest();
   xmlhttp.open("PUT","https://api.spotify.com/v1/me/player/play?device_id=" + device_id, true);
   xmlhttp.setRequestHeader("Authorization", "Bearer " + access_token);
   xmlhttp.setRequestHeader("Content-Type", "application/json");
   xmlhttp.send('{"uris": ["spotify:track:' + id + '"]}');

    }

    function seekSong(time) {
     var xmlhttp = new XMLHttpRequest();
   xmlhttp.open("PUT","https://api.spotify.com/v1/me/player/seek?position_ms=" + time + '&device_id=' + device_id, true);
   xmlhttp.setRequestHeader("Authorization", "Bearer " + access_token);
   xmlhttp.send();
  }
  }
  };

module.exports = {
  player: function(songs) {
    var access_token;
    var device_id;

    songs = JSON.parse(decodeURI(songs.split("+").join("")));

    access_token = "BQASUWNfBG4jw-lq909K7roOdQaBqwHO7wtFQ6PqO0kkTDEYANhpZkANevOIwLcIxvbmyoHRcnjwTXRB-90wrzfr0RF9N3G3M8Wpkddgp1yqaCegZbnVOPXoAmcHrOnb7cdCxYz2M7Gu1tZdxoQaTjj1APHDhMkdGu_67lwuKyiIOE0m9rwNZt1eMIxOYY8hCOPpvKnIoL8UU6YW3TXO3a4aS0qn_K4H0GNrihOkFmciRH0dTQE-V9FvRaJkkM8-27ZisEP_u2x67Eiy_IXA-hBmtHvPl-ORhrDkqOlSqobi0KNP05YmChTM-jUOB1SFyg";
    device_id = "028544928f36409f9fea78fc410396306b81dc08";

    var spotifyApi = new SpotifyWebApi();

    spotifyApi.setAccessToken(access_token);

    for (var i = 0; i < songs.length; i++) {
      var element = songs[i];
      console.log(element.id);
      console.log(element.start);
      console.log(element.end);

      setSong(element.id)
      sleep(800)
      seekSong(element.start)
      var timeout = parseInt(element.end) >= 0 ? parseInt(element.end) : 500
      sleep(2000 + timeout)
    }

    spotifyApi.pause({"device_id": device_id});
  },

    setSong: function(id) {
      $.ajax({
        url: 'https://api.spotify.com/v1/me/player/play?device_id=' + device_id,
        data: '{"uris": ["spotify:track:' + id + '"]}',
        type: 'PUT',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + access_token);
            xhr.setRequestHeader("Content-Type", "application/json");
        }
      })
    },

    seekSong: function(time) {
      $.ajax({
        url: 'https://api.spotify.com/v1/me/player/seek?position_ms=' + time + '&device_id=' + device_id,
        type: 'PUT',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + access_token);
        }
      })
    },

    sleep: function(milliseconds) {
      return new Promise(resolve => setTimeout(resolve, milliseconds));
    }
  };

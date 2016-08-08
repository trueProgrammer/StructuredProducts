var configYouTubePlayer = {
    // ID видеоролика - можно взять из адресной строки на странице ролика на youtube.
    videoID: '_EZkD0nDp84',

    //minHeight: 315,
    
    //maxWidth: 650,

    trackProgress: true,

    trackPlaybackQuality: true,

    // Настройка API Youtube
    // @see https://developers.google.com/youtube/player_parameters?playerVersion=HTML5#Parameters
    playerVars: {
        'autohide': 1,
        'autoplay': 0,
        'rel': 0,
        'theme': 'light'
    }
};
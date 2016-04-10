window.onload = function() {
    new Ya.share({
        element: 'the_share',
        elementStyle: {
            'type': 'none',
            'quickServices': ['vkontakte', 'facebook', 'twitter']
        },
        link: 'http://xstrum.ru',
        title: 'Xstrum - структурные продукты.',
        serviceSpecific: {
            twitter: {
                title: '@xstrum Xstrum - структурные продукты'
            }
        }
    });
};
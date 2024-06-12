function changeLanguage(lang) {
    if (window.location.href.indexOf('?') > -1) {
        window.location.href = window.location.href.substring(0, window.location.href.indexOf('?')) + '?lang=' + lang;
    } else {
        window.location.href += '?lang=' + lang;
    }

    if (localStorage.getItem('lang') !== null) {
        localStorage.removeItem('lang');
    }
    localStorage.setItem('lang', lang);
}

// function changeLanguage(lang) {
//     if (window.location.href.indexOf('?') > -1) {
//         window.location.href = window.location.href.substring(0, window.location.href.indexOf('?')) + '?lang=' + lang;
//     } else {
//         window.location.href += '?lang=' + lang;
//     }
//
//     if (localStorage.getItem('lang') !== null) {
//         localStorage.removeItem('lang');
//     }
//     localStorage.setItem('lang', lang);
//     window.location.replace(window.location.href);
// }

//
// function changeLanguage(lang) {
//     var newURL = window.location.href;
//
//     if (window.location.href.indexOf('?') > -1) {
//         newURL = window.location.href.substring(0, window.location.href.indexOf('?')) + '?lang=' + lang;
//     } else {
//         newURL += '?lang=' + lang;
//     }
//
//     if (localStorage.getItem('lang') !== null) {
//         localStorage.removeItem('lang');
//     }
//     localStorage.setItem('lang', lang);
//
//     // make a new request
//     window.location.replace(newURL);
// }
import { getButtonElement } from '../dist/utils/domUtils.js';
var logout = function (event) {
    event.preventDefault();
    localStorage.removeItem('username');
    window.history.replaceState(null, '', '/front-end/html/auth/index.html');
    window.location.href = '/front-end/html/auth/index.html';
};
var moveToLogin = function (event) {
    event.preventDefault();
    window.location.href = '/front-end/html/auth/index.html';
};
if (getButtonElement('logout')) {
    var logoutElement = getButtonElement('logout');
    logoutElement.addEventListener('click', logout);
}
if (getButtonElement('move-to-login')) {
    var loginButton = getButtonElement('move-to-login');
    loginButton.addEventListener('click', moveToLogin);
}
if (getButtonElement('back')) {
    var backBtn = getButtonElement('back');
    backBtn.addEventListener('click', function () {
        localStorage.removeItem('title');
        localStorage.removeItem('isbn');
        window.history.back();
    });
}
//# sourceMappingURL=app.js.map
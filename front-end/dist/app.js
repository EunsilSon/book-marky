import { getButtonElement } from '../dist/utils/domUtils.js';
var moveToLogin = function (event) {
    event.preventDefault();
    window.location.href = '/html/auth/index.html';
};
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
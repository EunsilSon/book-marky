import { getElementById } from '../utils/domUtils.js';
export var renderSecureQuestion = function (containerId, question) {
    var container = getElementById(containerId);
    var div = document.createElement('div');
    div.textContent = question;
    container.appendChild(div);
};
//# sourceMappingURL=renderUtils.js.map
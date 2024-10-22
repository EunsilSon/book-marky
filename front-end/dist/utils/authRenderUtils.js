import { getElementById } from './domUtils.js';
export var renderSecureQuestion = function (containerId, question) {
    var container = getElementById(containerId);
    var p = document.createElement('p');
    p.textContent = question;
    container.appendChild(p);
};
//# sourceMappingURL=authRenderUtils.js.map
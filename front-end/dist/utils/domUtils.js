export var getElementById = function (elementId) {
    var element = document.getElementById(elementId);
    return element;
};
export var getButtonElement = function (buttonId) {
    var buttonElement = document.getElementById(buttonId);
    return buttonElement;
};
export var getFormElement = function (formId) {
    var formElement = document.getElementById(formId);
    return formElement;
};
export var getInputElement = function (inputId) {
    var inputElement = document.getElementById(inputId);
    return inputElement;
};
export var getInputValue = function (inputId) {
    var inputElement = document.getElementById(inputId);
    return inputElement ? inputElement.value : '';
};
export var setInputValue = function (inputId, value) {
    var inputElement = document.getElementById(inputId);
    if (inputElement) {
        inputElement.value = value;
    }
};
export var showError = function (errorMessage) {
    var errorElement = document.getElementById('error-message');
    if (errorElement) {
        errorElement.textContent = errorMessage;
        errorElement.style.display = 'block';
    }
};
export var showAlert = function (message) {
    alert(message);
};
export var handleError = function (error) {
    var _a;
    if (axios.isAxiosError(error)) {
        console.error('Axios Error:', ((_a = error.response) === null || _a === void 0 ? void 0 : _a.data.message) || 'No error message');
        return error.response.status;
    }
};
//# sourceMappingURL=domUtils.js.map
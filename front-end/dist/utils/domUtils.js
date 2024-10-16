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
//# sourceMappingURL=domUtils.js.map
export const getInputValue = (inputId: string): string => {
    const inputElement = document.getElementById(inputId) as HTMLInputElement;
    return inputElement ? inputElement.value : '';
};

export const setInputValue = (inputId: string, value: string): void => {
    const inputElement = document.getElementById(inputId) as HTMLInputElement;
    if (inputElement) {
        inputElement.value = value;
    }
};

export const showError = (errorMessage: string): void => {
    const errorElement = document.getElementById('error-message');
    if (errorElement) {
        errorElement.textContent = errorMessage;
        errorElement.style.display = 'block';
    }
}
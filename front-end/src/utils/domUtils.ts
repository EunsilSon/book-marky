declare var axios: any;

export const getElementById = (elementId: string): HTMLElement => {
    const element = document.getElementById(elementId) as HTMLElement | null;
    return element;
};

export const getButtonElement = (buttonId: string): HTMLButtonElement => {
    const buttonElement = document.getElementById(buttonId) as HTMLButtonElement | null;
    return buttonElement;
};

export const getFormElement = (formId: string): HTMLFormElement => {
    const formElement = document.getElementById(formId) as HTMLFormElement | null;
    return formElement;
};

export const getInputElement = (inputId: string): HTMLInputElement => {
    const inputElement = document.getElementById(inputId) as HTMLInputElement | null;
    return inputElement;
};

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

export const showAlert = (message) => {
    alert(message);
};

export const handleError = (error: any) => {
    if (axios.isAxiosError(error)) {
        console.error('Axios Error:', error.response?.data.message || 'No error message');
        return error.response.status;
    } 
}
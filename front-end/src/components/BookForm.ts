import { getAllBooks } from '../services/bookService.js';
import { getButtonElement, getFormElement, getInputElement, getInputValue, showError, showAlert} from '../utils/domUtils.js';


document.addEventListener('DOMContentLoaded', () => {
    getAllBooks('id', 0); // default
})


const orderBooks = (event: Event) => {
    const checkbox = event.target as HTMLInputElement;
    getAllBooks(checkbox.id, 0);
}


const orderCheckBoxes = document.querySelectorAll('input[name="order"]') as NodeListOf<HTMLInputElement>;
orderCheckBoxes.forEach((checkbox) => {
    checkbox.addEventListener('change', (event) => {
        if ((event.target as HTMLInputElement).checked) {
            orderCheckBoxes.forEach((cb) => {
                if (cb != event.target) {
                    cb.checked = false;
                }
            });
        }

        orderBooks(event);
    });
});

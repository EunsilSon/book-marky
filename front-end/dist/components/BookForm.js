import { getAllBooks } from '../services/bookService.js';
document.addEventListener('DOMContentLoaded', function () {
    getAllBooks('id', 0); // default
});
var orderBooks = function (event) {
    var checkbox = event.target;
    getAllBooks(checkbox.id, 0);
};
var orderCheckBoxes = document.querySelectorAll('input[name="order"]');
orderCheckBoxes.forEach(function (checkbox) {
    checkbox.addEventListener('change', function (event) {
        if (event.target.checked) {
            orderCheckBoxes.forEach(function (cb) {
                if (cb != event.target) {
                    cb.checked = false;
                }
            });
        }
        orderBooks(event);
    });
});
//# sourceMappingURL=BookForm.js.map
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g = Object.create((typeof Iterator === "function" ? Iterator : Object).prototype);
    return g.next = verb(0), g["throw"] = verb(1), g["return"] = verb(2), typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (g && (g = 0, op[0] && (_ = 0)), _) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
import { getPassages, getPassageDetail, getDeletedPassages, updatePassage, deletePassage, createPassage, restorePassage } from "../services/passageService.js";
import { getBookDetail } from "../services/bookService.js";
import { showError } from "../utils/domUtils.js";
import { renderBookDetail } from "../utils/bookRenderUtils.js";
import { renderPassages, renderPassageDetail, renderPassageForm, renderDeletedPassages } from "../utils/passageRenderUtils.js";
import { showAlert } from "../utils/domUtils.js";
document.addEventListener('DOMContentLoaded', function () { return __awaiter(void 0, void 0, void 0, function () {
    var currentPath, bookId, bookResponse, passagesResponse, passageId, passageResponse, deletedPassages;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                currentPath = window.location.pathname;
                if (!currentPath.endsWith('all.html')) return [3 /*break*/, 3];
                bookId = new URLSearchParams(window.location.search).get('id');
                return [4 /*yield*/, getBookDetail(bookId)];
            case 1:
                bookResponse = _a.sent();
                renderBookDetail(bookResponse);
                return [4 /*yield*/, getPassages(bookId, 0)];
            case 2:
                passagesResponse = _a.sent();
                renderPassages(passagesResponse.data);
                _a.label = 3;
            case 3:
                if (!(currentPath.endsWith('detail.html') || currentPath.endsWith('update.html'))) return [3 /*break*/, 5];
                passageId = new URLSearchParams(window.location.search).get('id');
                return [4 /*yield*/, getPassageDetail(passageId)];
            case 4:
                passageResponse = _a.sent();
                renderPassageDetail(passageResponse.data, false);
                _a.label = 5;
            case 5:
                if (currentPath.endsWith('create.html')) {
                    renderPassageForm();
                }
                if (!currentPath.endsWith('deleted.html')) return [3 /*break*/, 7];
                return [4 /*yield*/, getDeletedPassages()];
            case 6:
                deletedPassages = _a.sent();
                renderDeletedPassages(deletedPassages.data);
                _a.label = 7;
            case 7: return [2 /*return*/];
        }
    });
}); });
export var updatePassageProcess = function (passage) { return __awaiter(void 0, void 0, void 0, function () {
    return __generator(this, function (_a) {
        try {
            updatePassage(passage);
        }
        catch (error) {
            return [2 /*return*/, showError(error)];
        }
        return [2 /*return*/];
    });
}); };
export var deletePassageProcess = function (passageId) { return __awaiter(void 0, void 0, void 0, function () {
    return __generator(this, function (_a) {
        try {
            deletePassage(passageId);
        }
        catch (error) {
            return [2 /*return*/, showError(error)];
        }
        return [2 /*return*/];
    });
}); };
export var createPassageProcess = function (isbn, content, pageNum) { return __awaiter(void 0, void 0, void 0, function () {
    var newPassage;
    return __generator(this, function (_a) {
        try {
            newPassage = {
                isbn: isbn,
                content: content,
                pageNum: pageNum,
            };
            createPassage(newPassage);
        }
        catch (error) {
            return [2 /*return*/, showError(error)];
        }
        return [2 /*return*/];
    });
}); };
export var deletedPassageProcess = function () { return __awaiter(void 0, void 0, void 0, function () {
    var response;
    return __generator(this, function (_a) {
        try {
            response = getDeletedPassages();
            showAlert('삭제되었습니다. 이전 페이지로 이동합니다.');
            window.history.back();
        }
        catch (error) {
            return [2 /*return*/, showError(error)];
        }
        return [2 /*return*/];
    });
}); };
export var restorePassageProcess = function (passageId) { return __awaiter(void 0, void 0, void 0, function () {
    var response;
    return __generator(this, function (_a) {
        try {
            response = restorePassage(passageId);
            showAlert('복구되었습니다. 메인 페이지로 이동합니다.');
            window.location.href = "../../html/book/index.html";
        }
        catch (error) {
            return [2 /*return*/, showError(error)];
        }
        return [2 /*return*/];
    });
}); };
//# sourceMappingURL=PassageForm.js.map
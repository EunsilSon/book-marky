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
import { join, checkDuplicateUsername, checkDuplicateNickname, checkDuplicateTel } from '../services/authService.js';
import { getFormElement, getInputElement, getInputValue, showError, showAlert } from '../utils/domUtils.js';
var usernameInput = getInputElement('username');
var nicknameInput = getInputElement('nickname');
var telephoneInput = getInputElement('telephone');
var joinForm = getFormElement('join-form');
var checkUsername = function (event) { return __awaiter(void 0, void 0, void 0, function () {
    var response, error_1;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                event.preventDefault();
                _a.label = 1;
            case 1:
                _a.trys.push([1, 3, , 4]);
                return [4 /*yield*/, checkDuplicateUsername(usernameInput.value)];
            case 2:
                response = _a.sent();
                if (response) {
                    showAlert('중복되는 아이디입니다. 다시 입력하세요.');
                    usernameInput.value = '';
                }
                return [3 /*break*/, 4];
            case 3:
                error_1 = _a.sent();
                showError('Username is duplicated.');
                console.error('Username Error:', error_1);
                return [3 /*break*/, 4];
            case 4: return [2 /*return*/];
        }
    });
}); };
var checkNickname = function (event) { return __awaiter(void 0, void 0, void 0, function () {
    var response, error_2;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                event.preventDefault();
                _a.label = 1;
            case 1:
                _a.trys.push([1, 3, , 4]);
                return [4 /*yield*/, checkDuplicateNickname(nicknameInput.value)];
            case 2:
                response = _a.sent();
                if (response) {
                    showAlert('중복되는 닉네임입니다. 다시 입력하세요.');
                    usernameInput.value = '';
                }
                return [3 /*break*/, 4];
            case 3:
                error_2 = _a.sent();
                showError('Nickname is duplicated.');
                console.error('Nickname Error:', error_2);
                return [3 /*break*/, 4];
            case 4: return [2 /*return*/];
        }
    });
}); };
var checkTelephone = function (event) { return __awaiter(void 0, void 0, void 0, function () {
    var response, error_3;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                event.preventDefault();
                _a.label = 1;
            case 1:
                _a.trys.push([1, 3, , 4]);
                return [4 /*yield*/, checkDuplicateTel(telephoneInput.value)];
            case 2:
                response = _a.sent();
                if (response) {
                    showAlert('중복되는 연락처입니다. 다시 입력하세요.');
                    usernameInput.value = '';
                }
                return [3 /*break*/, 4];
            case 3:
                error_3 = _a.sent();
                showError('Telephone is duplicated.');
                console.error('Telephone Error:', error_3);
                return [3 /*break*/, 4];
            case 4: return [2 /*return*/];
        }
    });
}); };
var joinFormProcess = function (event) { return __awaiter(void 0, void 0, void 0, function () {
    var usernameValue, nicknameValue, telephoneValue, passwordValue, secureQuestionIdValue, answerContentValue, response, error_4;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                event.preventDefault();
                usernameValue = usernameInput.value;
                nicknameValue = nicknameInput.value;
                telephoneValue = telephoneInput.value;
                passwordValue = getInputValue('password');
                secureQuestionIdValue = getInputValue('secure-question-id');
                answerContentValue = getInputValue('answer-content');
                _a.label = 1;
            case 1:
                _a.trys.push([1, 3, , 4]);
                return [4 /*yield*/, join({
                        username: usernameValue,
                        password: passwordValue,
                        nickname: nicknameValue,
                        telephone: telephoneValue,
                        secureQuestionId: secureQuestionIdValue,
                        answerContent: answerContentValue,
                    })];
            case 2:
                response = _a.sent();
                console.log('Join Result:', response);
                return [3 /*break*/, 4];
            case 3:
                error_4 = _a.sent();
                showError('Join failed. Please check your credentials.');
                console.error('Join Error:', error_4);
                return [3 /*break*/, 4];
            case 4: return [2 /*return*/];
        }
    });
}); };
usernameInput.addEventListener('change', checkUsername);
nicknameInput.addEventListener('change', checkNickname);
telephoneInput.addEventListener('change', checkTelephone);
joinForm.addEventListener('submit', joinFormProcess);
//# sourceMappingURL=JoinForm.js.map
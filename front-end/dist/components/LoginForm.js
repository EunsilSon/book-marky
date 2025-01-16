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
import { login } from '../services/authService.js';
import { getButtonElement, getFormElement, getInputElement, getInputValue, showAlert } from '../utils/domUtils.js';
var loginForm = getFormElement('login-form');
var joinButton = getButtonElement('move-to-join');
var pwButton = getButtonElement('move-to-pw');
var loginFormProcess = function (event) { return __awaiter(void 0, void 0, void 0, function () {
    var usernameValue, passwordValue, response, error_1;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                event.preventDefault();
                usernameValue = getInputValue('username');
                passwordValue = getInputValue('password');
                _a.label = 1;
            case 1:
                _a.trys.push([1, 3, , 4]);
                return [4 /*yield*/, login({
                        username: usernameValue,
                        password: passwordValue,
                    })];
            case 2:
                response = _a.sent();
                console.log('Login Result:', response);
                if (response.status == 200) {
                    localStorage.setItem('username', response.data);
                    showAlert('로그인에 성공했습니다. 메인 페이지로 이동합니다.');
                    window.location.href = '/html/book/index.html';
                }
                if (response.status == 401) {
                    getInputElement('username').value = '';
                    getInputElement('password').value = '';
                    showAlert('아이디 또는 비밀번호가 맞지 않습니다. 다시 입력하세요.');
                }
                return [3 /*break*/, 4];
            case 3:
                error_1 = _a.sent();
                console.error('Login Error:', error_1);
                return [3 /*break*/, 4];
            case 4: return [2 /*return*/];
        }
    });
}); };
var moveToJoin = function (event) {
    event.preventDefault();
    window.location.href = '/html/auth/join.html';
};
var moveToPw = function (event) {
    event.preventDefault();
    window.location.href = '/html/auth/request-pw.html';
};
loginForm.addEventListener('submit', loginFormProcess);
joinButton.addEventListener('click', moveToJoin);
pwButton.addEventListener('click', moveToPw);
//# sourceMappingURL=LoginForm.js.map
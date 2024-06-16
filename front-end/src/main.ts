/**
 * Vue.js 애플리케이션의 진입점 역할로, 애플리케이션 설정과 초기화를 담당하며 Vue 인스턴스가 DOM에 연결되는 과정 관리
 * - DOM 마운트 : Vue 애플리케이션이 HTML 문서의 특정 부분을 제어할 수 있게 하고, 동적인 사용자 인터페이스 구현
 *      1. 초기 렌더링 : Vue 컴포넌트가 지정된 DOM 요소에 렌더링
 *      2. 반응성 : 애플리케이션 상태 변화에 따라 DOM 자동 업데이트
 *      3. 컴포넌트 관리 : Vue 컴포넌트 라이프사이클 훅을 통해 컴포넌트 생성, 업데이트, 소멸 등의 과정 관리
 */

import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import "./styles/index.css";

createApp(App).use(router).mount("#app");

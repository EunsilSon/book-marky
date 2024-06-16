<!--
Vue.js 애플리케이션의 진입점이자 최상위 루트 컴포넌트
- 라우팅 : <router-view> 태그로 vue router를 통한 페이지 라우팅 처리]
- 전역 스타일 및 구조 정의
- 다른 컴포넌트 통합
-->

<template>
  <nav></nav>
  <router-view />
</template>

<style lang="css">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: black;
}

nav {
  padding: 30px;
}
</style>

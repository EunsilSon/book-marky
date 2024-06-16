<template>
  <div class="title">
    <h1>Login</h1>
    <div class="user_box">
      <input type="email" id="email" name="email" placeholder="Email" />
      <input type="password" id="pw" name="pw" placeholder="Password" />
      <div class="user_box_btn">
        <button>로그인</button>
      </div>
      <router-link to="/join">Create an account</router-link>
      <router-link to="/pw">Forgot password?</router-link>
    </div>
  </div>
</template>

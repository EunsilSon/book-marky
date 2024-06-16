<template>
  <div class="title">
    <h1>Join</h1>
    <div class="user_box">
      <input type="text" id="name" name="name" placeholder="Username" />
      <input type="email" id="email" name="email" placeholder="Email" />
      <input type="password" id="pw" name="pw" placeholder="Password" />
      <div class="user_box_btn">
        <button>확인</button>
        <router-link to="/">
          <button>뒤로가기</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

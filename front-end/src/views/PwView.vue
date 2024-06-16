<template>
  <div class="title">
    <h1>Reset you password</h1>
    <p>
      사용자 계정에 사용된 이메일 주소를 입력하시면 비밀번호 재설정 링크를
      보내드립니다.
    </p>
    <div class="user_box">
      <input type="email" id="email" name="email" placeholder="Email" />
      <div class="user_box_btn">
        <button>확인</button>
        <router-link to="/">
          <button>뒤로가기</button>
        </router-link>
      </div>
    </div>
  </div>
</template>

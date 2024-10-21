export interface User {
    username: string;
    password: string;
    nickname?: string;
    telephone?: string;
    secureQuestionId?: string;
    answerContent?: string;
}

export interface UserPasswordAnswer {
    username: string;
    answer: string;
}

export interface UserPasswordToken {
    username: string;
    password: string;
    token: string;
}
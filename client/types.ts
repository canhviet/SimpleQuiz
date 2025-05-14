import { HttpContext, HttpHeaders, HttpParams } from '@angular/common/http';

export interface Options {
    headers?:
        | HttpHeaders
        | {
              [header: string]: string | string[];
          };
    observe?: 'body';
    context?: HttpContext;
    params?:
        | HttpParams
        | {
              [param: string]:
                  | string
                  | number
                  | boolean
                  | ReadonlyArray<string | number | boolean>;
          };
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
    transferCache?:
        | {
              includeHeaders?: string[];
          }
        | boolean;
}

export interface PaginationParams {
    [param: string]:
        | string
        | number
        | boolean
        | ReadonlyArray<string | number | boolean>;
    pageNo: number;
    pageSize: number;
}

export interface Option {
  id?: number;
  title: string;
  isCorrect: boolean;
}

export interface TokenResponse {
    userId: string;
    accessToken: string;
    refreshToken: string;
}

export interface Question {
  id?: number;
  title: string;
  options: Option[];
}

export interface Quizz {
  id?: number;
  userId: number;
  name: String;
  questions: Question[];
}

export interface QuizResponse {
  id?: number;
  userId: number;
  name: String;
  questions: Question[];
  updatedAt: Date;
}

export interface SignInRequest {
    username: String;
    password: String;
}

export interface DataResponse {
    status: number;
    message: string;
    data: any;
}

export interface JwtPayload {
  roles: string[];
  username: string;
  sub: string;
  iat: number;
  exp: number;
}

export interface HistoryResponse {
  quizzId: number;
  score: String;
  createAt: Date;
}

export interface HistoryRequest {
  score: String;
  userId: number;
  quizzId: number;
}

export interface GenAI {
    topic: String;
    numQuestions: number;
    userId: number;
    quizzName: String;
}
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
  is_correct: boolean;
}

export interface Question {
  id?: number;
  title: string;
  options: Option[];
}

export interface Quizz {
  id?: number;
  userId: number;
  title: string;
  questions: Question[];
}

export interface SignInRequest {
    username: String;
    password: String;
}

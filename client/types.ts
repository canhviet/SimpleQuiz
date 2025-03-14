export interface Options {
  id?: number;
  title: string;
  is_correct: boolean;
}

export interface Questions {
  id?: number;
  title: string;
  options: Options[];
}

export interface Quizz {
  id?: number;
  title: string;
  questions: Questions[];
}

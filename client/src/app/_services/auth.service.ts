import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { CookieService } from 'ngx-cookie-service';
import { TokenResponse } from '../../../types';

const AUTH_API = 'http://localhost:8081/auth/';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private tokenData: TokenResponse | null = null;

    constructor(private apiService: ApiService, private cookieService: CookieService) {
        const tokenString = this.cookieService.get('token');
        if (tokenString) {
            this.tokenData = JSON.parse(tokenString);
        }
    }

    login(username: String, password: String): Observable<any> {
        return this.apiService.post(
            AUTH_API + 'access-token',
            {
                username,
                password,
            },
            httpOptions
        );
    }

    refresh(refresh_token: string): Observable<any> {
        return this.apiService.post(AUTH_API + 'refresh-token', {}, {});
    }

    register(
        username: string,
        email: string,
        password: string
    ): Observable<any> {
        return this.apiService.post(
            AUTH_API + 'register',
            {
                username,
                email,
                password,
            },
            httpOptions
        );
    }

    logout(): Observable<any> {
        return this.apiService.post(AUTH_API + 'remove-token', {}, httpOptions);
    }

    forgotPassword(data: String): Observable<any> {
        return this.apiService.post(AUTH_API + 'forgot-password', data, {
            responseType: 'text' as 'json',
        });
    }

    resetPassword(data: String): Observable<any> {
        return this.apiService.post(AUTH_API + 'reset-password', data, {
            responseType: 'text' as 'json',
        });
    }

    changePassword(data: any): Observable<any> {
        return this.apiService.post(AUTH_API + 'change-password', data, {
            responseType: 'text' as 'json',
        });
    }

    setTokenData(data: TokenResponse) {
        this.tokenData = data;
        this.cookieService.set('token', JSON.stringify(data), 7);
    }

    getTokenData(): TokenResponse | null {
        return this.tokenData;
    }

    removeToken() {
        this.cookieService.delete('token');
    }
}


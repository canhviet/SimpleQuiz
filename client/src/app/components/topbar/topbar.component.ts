import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../_services/auth.service';

@Component({
    selector: 'app-topbar',
    standalone: false,
    templateUrl: './topbar.component.html',
    styleUrl: './topbar.component.css'
})
export class TopbarComponent {
    constructor(private router: Router,
        private authService: AuthService,
        private toastr: ToastrService) { }
    logout() {
        this.authService.removeToken();

        this.toastr.success(
            'Đăng xuất thành công!',
            'Thành công'
        );

        this.router.navigate(['']);
    }
}

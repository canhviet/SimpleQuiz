import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YourQuizzComponent } from './your-quizz.component';

describe('YourQuizzComponent', () => {
  let component: YourQuizzComponent;
  let fixture: ComponentFixture<YourQuizzComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [YourQuizzComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YourQuizzComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

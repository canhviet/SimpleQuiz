import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddWithAIComponent } from './add-with-ai.component';

describe('AddWithAIComponent', () => {
  let component: AddWithAIComponent;
  let fixture: ComponentFixture<AddWithAIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddWithAIComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddWithAIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

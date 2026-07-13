import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserExport } from './user-export';

describe('UserExport', () => {
  let component: UserExport;
  let fixture: ComponentFixture<UserExport>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserExport],
    }).compileComponents();

    fixture = TestBed.createComponent(UserExport);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

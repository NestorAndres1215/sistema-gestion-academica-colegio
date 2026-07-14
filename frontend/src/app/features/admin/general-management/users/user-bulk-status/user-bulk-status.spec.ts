import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserBulkStatus } from './user-bulk-status';

describe('UserBulkStatus', () => {
  let component: UserBulkStatus;
  let fixture: ComponentFixture<UserBulkStatus>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserBulkStatus],
    }).compileComponents();

    fixture = TestBed.createComponent(UserBulkStatus);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

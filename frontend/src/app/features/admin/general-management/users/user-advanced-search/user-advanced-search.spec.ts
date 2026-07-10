import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAdvancedSearch } from './user-advanced-search';

describe('UserAdvancedSearch', () => {
  let component: UserAdvancedSearch;
  let fixture: ComponentFixture<UserAdvancedSearch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserAdvancedSearch],
    }).compileComponents();

    fixture = TestBed.createComponent(UserAdvancedSearch);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

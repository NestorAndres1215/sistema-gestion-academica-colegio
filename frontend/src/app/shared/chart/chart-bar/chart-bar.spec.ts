import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartBar } from './chart-bar';

describe('ChartBar', () => {
  let component: ChartBar;
  let fixture: ComponentFixture<ChartBar>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChartBar],
    }).compileComponents();

    fixture = TestBed.createComponent(ChartBar);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

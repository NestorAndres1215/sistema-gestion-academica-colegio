import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChartDonut } from './chart-donut';

describe('ChartDonut', () => {
  let component: ChartDonut;
  let fixture: ComponentFixture<ChartDonut>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChartDonut],
    }).compileComponents();

    fixture = TestBed.createComponent(ChartDonut);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

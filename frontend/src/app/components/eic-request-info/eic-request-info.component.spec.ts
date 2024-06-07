import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EicRequestInfoComponent } from './eic-request-info.component';

describe('EicRequestInfoComponent', () => {
  let component: EicRequestInfoComponent;
  let fixture: ComponentFixture<EicRequestInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EicRequestInfoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EicRequestInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

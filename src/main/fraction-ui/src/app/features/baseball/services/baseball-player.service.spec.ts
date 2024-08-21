import { TestBed } from '@angular/core/testing';

import { BaseballPlayerService } from './baseball-player.service';

describe('BaseballPlayerService', () => {
  let service: BaseballPlayerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaseballPlayerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

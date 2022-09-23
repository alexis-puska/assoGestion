import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IConfigurationDon, ConfigurationDon } from '../configuration-don.model';
import { ConfigurationDonService } from '../service/configuration-don.service';

import { ConfigurationDonRoutingResolveService } from './configuration-don-routing-resolve.service';

describe('ConfigurationDon routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ConfigurationDonRoutingResolveService;
  let service: ConfigurationDonService;
  let resultConfigurationDon: IConfigurationDon | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ConfigurationDonRoutingResolveService);
    service = TestBed.inject(ConfigurationDonService);
    resultConfigurationDon = undefined;
  });

  describe('resolve', () => {
    it('should return IConfigurationDon returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConfigurationDon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConfigurationDon).toEqual({ id: 123 });
    });

    it('should return new IConfigurationDon if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConfigurationDon = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultConfigurationDon).toEqual(new ConfigurationDon());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ConfigurationDon })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultConfigurationDon = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultConfigurationDon).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});

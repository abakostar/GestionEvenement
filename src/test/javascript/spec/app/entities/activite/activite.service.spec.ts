import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ActiviteService } from 'app/entities/activite/activite.service';
import { IActivite, Activite } from 'app/shared/model/activite.model';

describe('Service Tests', () => {
  describe('Activite Service', () => {
    let injector: TestBed;
    let service: ActiviteService;
    let httpMock: HttpTestingController;
    let elemDefault: IActivite;
    let expectedResult: IActivite | IActivite[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ActiviteService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Activite(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, currentDate, false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date_activite: currentDate.format(DATE_FORMAT),
            heure_debut: currentDate.format(DATE_TIME_FORMAT),
            heure_fin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Activite', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date_activite: currentDate.format(DATE_FORMAT),
            heure_debut: currentDate.format(DATE_TIME_FORMAT),
            heure_fin: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date_activite: currentDate,
            heure_debut: currentDate,
            heure_fin: currentDate,
          },
          returnedFromService
        );

        service.create(new Activite()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Activite', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            description: 'BBBBBB',
            date_activite: currentDate.format(DATE_FORMAT),
            heure_debut: currentDate.format(DATE_TIME_FORMAT),
            heure_fin: currentDate.format(DATE_TIME_FORMAT),
            etatclos: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date_activite: currentDate,
            heure_debut: currentDate,
            heure_fin: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Activite', () => {
        const returnedFromService = Object.assign(
          {
            nom: 'BBBBBB',
            description: 'BBBBBB',
            date_activite: currentDate.format(DATE_FORMAT),
            heure_debut: currentDate.format(DATE_TIME_FORMAT),
            heure_fin: currentDate.format(DATE_TIME_FORMAT),
            etatclos: true,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date_activite: currentDate,
            heure_debut: currentDate,
            heure_fin: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Activite', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

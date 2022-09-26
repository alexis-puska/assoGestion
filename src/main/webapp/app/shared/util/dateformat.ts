import { Injectable } from '@angular/core';
import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';

@Injectable()
export class MomentDateFormatter extends NgbDateParserFormatter {
  readonly DT_FORMAT = 'DD/MM/YYYY';
  mdt: any;
  parse(value: string): NgbDateStruct | null {
    if (value) {
      value = value.trim();
      dayjs.extend(customParseFormat);
      this.mdt = dayjs(value, this.DT_FORMAT);
    } else {
      return null;
    }
    if (this.mdt && dayjs.isDayjs(this.mdt) && this.mdt.isValid()) {
      return { year: this.mdt.year(), month: this.mdt.month() + 1, day: this.mdt.date() };
    }
    return null;
  }
  format(date: NgbDateStruct | null): string {
    let value;
    if (date) {
      this.mdt = dayjs(`${date.year}-${date.month}-${date.day}`);
      if (!this.mdt.isValid()) {
        value = '';
      }
      value = this.mdt.format(this.DT_FORMAT);
    } else {
      value = '';
    }
    return value;
  }
}

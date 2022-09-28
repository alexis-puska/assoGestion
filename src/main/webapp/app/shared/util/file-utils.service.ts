import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class FileUtilsService {
  static encodeFileName(fileName: string): string {
    return fileName
      .replace(/[&,~,{,},|,°,¨,^,£,$,¤,µ,*,`,',’,",#,%,:,+,=,@]/g, '')
      .replace(/[ ,`,',’]/g, '_')
      .replace(/[é,è,ê]/g, 'e')
      .replace(/[ù]/g, 'u')
      .replace(/[à,â]/g, 'a')
      .replace(/[ç]/g, 'c');
  }

  static getFileNameFromHeader(headers: HttpHeaders): string {
    let fileName = 'default.fic';
    const contentDisposition = headers.getAll('Content-Disposition');
    contentDisposition?.find(cd => {
      if (cd.startsWith('attachment;filename')) {
        fileName = cd.split('=')[1];
        fileName = fileName.replace(/"/g, '');
      }
    });
    return fileName;
  }
}

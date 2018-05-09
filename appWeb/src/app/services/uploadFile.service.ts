import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { GLOBAL } from './global';

@Injectable()
export class UploadFileService{
    public url:string;

    constructor(
        public _http: Http
    ){
        this.url = GLOBAL.url;
    }

    upLoadFile(formData){

            return this._http.post(this.url+'file/', formData)
                .pipe(map(res =>  {  return {
                                            code: res.status,
                                            data : res.text(),
                                            filename:this.searchFilename(res,"content-disposition")
                                        }; 
                                    } ));                
    }

    searchFilename(res, headerContent){
        let splitHeader = res.headers.get(headerContent).split(";");
        let fileName = '';
        for (let header of splitHeader) {
            if(header.indexOf("filename") > 0){
                fileName = header.split("=")[1]
            }
        };
        return fileName;
    }

}

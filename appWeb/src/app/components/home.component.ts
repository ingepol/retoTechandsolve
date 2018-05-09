import { Component } from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { Worker } from '../models/worker';
import { UploadFileService } from '../services/uploadFile.service';

@Component({
    selector: 'home',
    templateUrl: '../views/home.html',
    providers: [UploadFileService]
})
export class HomeComponent{
    public titulo:string;
    public message:string;
    public worker:Worker;

    public workers:Worker[];

    constructor(
         private _uploadFileService:UploadFileService
    ){
        this.titulo = "Webapp oficina de mudanza: Procesar Archivos";
        this.worker = new Worker('', new Date());
    }
    ngOnInit(){
        console.log('Se cargo el componente home home.component');
    }
    
    onSubmit(){
        console.log('hola');
    }

    fileChange(event) {
        let fileList: FileList = event.target.files;
        if(fileList.length > 0) {
            let file: File = fileList[0];
            let formData:FormData = new FormData();
            formData.append('file', file, file.name);
            var reader = new FileReader();
            this._uploadFileService.upLoadFile(formData).subscribe(
                response => {
                    if(response.code == 200){

                        this.worker.date = new Date();

                        if(typeof this.workers == "undefined"){
                            this.workers = [];    
                        }
                        this.workers.push(this.worker);

                        this.worker = new Worker('', new Date);
                        (<HTMLInputElement>document.getElementById("file")).value = "";
                        this.downloadFile(response);

                    }else{
                        console.log(response);
                    }
                },
                error => {
                    (<HTMLInputElement>document.getElementById("file")).value = "";
                    console.log(<any>error);
                }
            )
        }
    }

    downloadFile(res){
        let blob = new Blob([res.data], { type: 'text/plain' });
        let url = window.URL.createObjectURL(blob);
        let a = document.createElement('a');
        document.body.appendChild(a);
        a.setAttribute('style', 'display: none');
        a.href = url;
        a.download = res.filename;
        a.click();
        window.URL.revokeObjectURL(url);
        a.remove(); // remove the element
        window.open(url);
    }
}
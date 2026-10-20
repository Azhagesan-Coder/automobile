import { bootstrapApplication } from '@angular/platform-browser';
import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpErrorResponse, provideHttpClient, withInterceptors } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

type Row = Record<string, any>;
type Field = { key: string; label: string; type?: string; required?: boolean; options?: string[] };
const endpoints: Record<string, string> = { Inventory: '/invent', Customers: '/customers', Sales: '/sales', Service: '/service' };
const fields: Record<string, Field[]> = {
  Inventory: [{key:'vin',label:'VIN',required:true},{key:'make',label:'Make',required:true},{key:'model',label:'Model',required:true},{key:'color',label:'Color'},{key:'year',label:'Year',type:'number'},{key:'mileage',label:'Mileage',type:'number'},{key:'price',label:'Price',type:'number',required:true},{key:'category',label:'Category',options:['NEW','USED','ON_SALE'],required:true},{key:'stock',label:'Stock',type:'number',required:true},{key:'dateAdded',label:'Date added',type:'date'}],
  Customers: [{key:'name',label:'Full name',required:true},{key:'email',label:'Email',type:'email',required:true},{key:'mobile',label:'Mobile'},{key:'address',label:'Address'},{key:'preferences',label:'Preferences'},{key:'purchaseHistory',label:'Purchase history'}],
  Sales: [{key:'customerId',label:'Customer',type:'customer',required:true},{key:'carId',label:'Vehicle',type:'vehicle',required:true},{key:'amount',label:'Amount',type:'number',required:true},{key:'saleDate',label:'Sale date',type:'date',required:true}],
  Service: [{key:'vehicleId',label:'Vehicle',type:'vehicle',required:true},{key:'serviceType',label:'Service type',required:true},{key:'appointmentDate',label:'Appointment date',type:'date',required:true},{key:'cost',label:'Cost',type:'number'},{key:'notes',label:'Notes'}]
};

@Component({selector:'app-root',standalone:true,imports:[CommonModule,FormsModule],templateUrl:'./app.html'})
class App {
  http = inject(HttpClient);
  pages = ['Overview','Inventory','Customers','Sales','Service','Reports','Documents'];
  icons = ['◫','◇','♧','↗','⚙','▥','▤'];
  page = signal('Overview'); token = signal(sessionStorage.getItem('showroom-token') || '');
  data = signal<Record<string,Row[]>>({Inventory:[],Customers:[],Sales:[],Service:[]});
  busy = signal(false); saving = signal(false); error = signal(''); notice = signal(''); loaded = signal(false);
  modal = signal(false); editing: number | null = null; draft: Row = {}; search = signal(''); category = signal('All');
  username = ''; password = ''; registering = false;
  from = new Date(new Date().getFullYear(),0,1).getFullYear().toString() + '-01-01'; to = new Date().toISOString().slice(0,10);
  reports = signal<Row[]>([]); documentId = ''; uploaded = signal<Row|null>(null);
  formFields = computed(() => fields[this.page()] || []);
  rows = computed(() => (this.data()[this.page()] || []).filter(r => JSON.stringify(r).toLowerCase().includes(this.search().toLowerCase()) && (this.page() !== 'Inventory' || this.category() === 'All' || r['category'] === this.category())));
  revenue = computed(() => this.data()['Sales'].reduce((n,r) => n + Number(r['amount'] || 0),0));
  stock = computed(() => this.data()['Inventory'].reduce((n,r) => n + Number(r['stock'] || 0),0));
  columns = computed(() => (({Inventory:['Vehicle','Category','Price','Stock'],Customers:['Customer','Email','Mobile','Address'],Sales:['Reference','Customer','Vehicle','Amount','Date'],Service:['Reference','Vehicle','Service','Date','Status']} as Record<string,string[]>)[this.page()] || []));
  constructor(){ if(this.token()) void this.refresh(); }
  money(value: any){ return new Intl.NumberFormat('en-IN',{style:'currency',currency:'INR',maximumFractionDigits:0}).format(Number(value || 0)); }
  vehicle(id:any){ const v = this.data()['Inventory'].find(x=>String(x['id'])===String(id)); return v ? `${v['make']} ${v['model']}` : `Vehicle #${id}`; }
  customer(id:any){ return this.data()['Customers'].find(x=>String(x['id'])===String(id))?.['name'] || `Customer #${id}`; }
  async request<T>(method:string,path:string,body?:any):Promise<T>{return firstValueFrom(this.http.request<T>(method,`/api${path}`,{body}));}
  fail(e:unknown){const h=e as HttpErrorResponse; if(h.status===401){const signedIn=!!this.token();this.logout();this.error.set(signedIn ? 'Your session has expired. Please sign in again.' : 'Invalid username or password.');return;} this.error.set(typeof h.error==='string' ? h.error : h.error?.message || (h.status===0 ? 'Cannot reach the API gateway. Start the backend and check the proxy configuration.' : `Request failed (${h.status}). Please check your details and try again.`));}
  async auth(){this.saving.set(true);this.error.set('');try{if(this.registering){await firstValueFrom(this.http.post('/api/auth/register',{username:this.username,password:this.password},{responseType:'text'}));this.registering=false;this.notice.set('Account created. Sign in to continue.');}else{const r=await this.request<{token:string}>('POST','/auth/login',{username:this.username,password:this.password});sessionStorage.setItem('showroom-token',r.token);this.token.set(r.token);this.password='';await this.refresh();}}catch(e){this.fail(e);}finally{this.saving.set(false);}}
  logout(){sessionStorage.removeItem('showroom-token');this.token.set('');this.data.set({Inventory:[],Customers:[],Sales:[],Service:[]});this.loaded.set(false);this.password='';}
  async refresh(){this.busy.set(true);this.error.set('');const results=await Promise.allSettled(Object.entries(endpoints).map(async([name,path])=>({name,rows:await this.request<Row[]>('GET',path)})));let next={...this.data()};for(const r of results){if(r.status==='fulfilled')next[r.value.name]=r.value.rows;else this.fail(r.reason);}if(this.token())this.data.set(next);this.loaded.set(results.every(r=>r.status==='fulfilled'));this.busy.set(false);}
  go(p:string){this.page.set(p);this.search.set('');this.category.set('All');this.notice.set('');}
  open(row?:Row){this.editing=row?.['id'] ?? null;this.draft=row?{...row}:{};this.error.set('');this.modal.set(true);}
  async save(){this.saving.set(true);this.error.set('');try{const body:Row={};for(const f of this.formFields()){const value=this.draft[f.key];body[f.key]=f.type==='number'||f.key==='vehicleId' ? (value===''||value==null ? null : Number(value)) : (value ?? null);}const path=endpoints[this.page()]+(this.editing?`/${this.editing}`:this.page()==='Inventory'?'/add':'');await this.request(this.editing?'PUT':'POST',path,body);this.modal.set(false);await this.refresh();this.notice.set('Record saved successfully.');}catch(e){this.fail(e);}finally{this.saving.set(false);}}
  async remove(row:Row){if(!confirm(`Delete record #${row['id']}? This cannot be undone.`))return;this.saving.set(true);try{await this.request('DELETE',`${endpoints[this.page()]}/${row['id']}`);await this.refresh();this.notice.set('Record deleted.');}catch(e){this.fail(e);}finally{this.saving.set(false);}}
  async status(row:Row,event:Event){const value=(event.target as HTMLSelectElement).value;this.saving.set(true);try{await this.request('PATCH',`/service/${row['id']}/status?status=${encodeURIComponent(value)}`);await this.refresh();}catch(e){(event.target as HTMLSelectElement).value=row['status'];this.fail(e);}finally{this.saving.set(false);}}
  async report(){if(!this.from||!this.to||this.from>this.to){this.error.set('Choose a valid date range.');return;}this.busy.set(true);this.error.set('');try{this.reports.set(await this.request<Row[]>('GET',`/report/sales/monthly?from=${this.from}&to=${this.to}`));this.notice.set('Report generated.');}catch(e){this.fail(e);}finally{this.busy.set(false);}}
  async upload(event:Event){const file=(event.target as HTMLInputElement).files?.[0];if(!file)return;this.saving.set(true);const body=new FormData();body.append('file',file);try{const r=await this.request<Row>('POST','/files/upload',body);this.uploaded.set(r);this.documentId=String(r['id']);this.notice.set('Document uploaded.');}catch(e){this.fail(e);}finally{this.saving.set(false);}}
  async download(){if(!/^[1-9][0-9]*$/.test(this.documentId)){this.error.set('Enter a positive document ID.');return;}try{const r=await firstValueFrom(this.http.get(`/api/files/download/${this.documentId}`,{responseType:'blob',observe:'response'}));const url=URL.createObjectURL(r.body!);const a=document.createElement('a');a.href=url;a.download=this.uploaded()?.['id']==this.documentId?this.uploaded()!['fileName']:`document-${this.documentId}`;a.click();setTimeout(()=>URL.revokeObjectURL(url),1000);}catch(e){this.fail(e);}}
}
bootstrapApplication(App,{providers:[provideHttpClient(withInterceptors([(req,next)=>{const token=sessionStorage.getItem('showroom-token');return next(token&&!req.url.startsWith('/api/auth/')?req.clone({setHeaders:{Authorization:`Bearer ${token}`}}):req);}]))]}).catch(console.error);




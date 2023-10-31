#include <unistd.h>
#include <sys/wait.h>
#include <stdlib.h>



int main(int argc,char *argV[]){

	int pd[2];
	pipe(pd);
	pid_t pid;
	pid=fork();

	
	if(pid>0){
			int n=4;
			char buff[30];
			while(n!=0){
			
			int nn=read (0,buff,sizeof(buff));
			write(pd[1],buff,nn);
			if(nn==1){
			n=close (pd[0]);
			}
			}
	}
	//	sleep(5);

	char buff[12];
		int n=2;

		while(n>1){
		n=read (pd[0],buff,sizeof(buff));
		write(1,buff,n);
		}
		close(pd[0]);
		exit(0);
	return 0;
}

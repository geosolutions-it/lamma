import sys, traceback

def main():

        out_file = open("/opt/geobatch/conf/commons/pippo.txt","w")
        for arg in sys.argv: 1
            out_file.write("Argomento: "+arg+"\n")
        out_file.close()
    except KeyboardInterrupt:
        print "Shutdown requested...exiting"
    except Exception:
        traceback.print_exc(file=sys.stdout)
    sys.exit(0)

if __name__ == "__main__":
    main()
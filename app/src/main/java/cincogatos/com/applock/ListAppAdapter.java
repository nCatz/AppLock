package cincogatos.com.applock;


import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAppAdapter extends ArrayAdapter<AppInfo> {

    //Camps
    private Context context;
    private ArrayList<AppInfo> localList;
    public static final int ORDERBY_NAME_ASC = 0;
    public static final int ORDERBY_NAME_DES = 1;
    public static final int ORDERBY_BLOKED_ASC = 2;
    public static final int ORDERBY_BLOKED_DES = 3;


    /*
    * IMPORTANTE: hay que plantearse el usar un Sigleton para la lista de aplicaciones, ya que se va a cambiar
    * el estado de bloqueado cada vez que se pulsa el icono del candado. Además hay que tener cuidado cuando
    * permitamos el filtrado de datos ya que las posiciones de la lista local puede que no se correspondan
    * con las de la lista completa de las app. Una posible solucion es utilizar el packageName de cada objeto
    * de la clase AppInfo (si es que es unico que no lo se) y hacer un metodo a la lista que permita localizar
    * un objeto por su packageName. De este modo buscamos el objeto antes de cambiar el estado de bloqueado
    * y así nos aseguramos de estar cambiando el estado del objeto correcto.
    *
    * */
    //Construct
    public ListAppAdapter(Context context, ArrayList<AppInfo> appInfoList) {
        super(context, R.layout.item_list_app);
        this.context = context;
        this.localList = new ArrayList<AppInfo>(appInfoList);
        addAll(appInfoList);
    }

    //Overray Methods
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View rootView = convertView;
        final AppInfoHolder holder;

        if(convertView == null){

            holder = new AppInfoHolder();
            rootView = LayoutInflater.from(this.context).inflate(R.layout.item_list_app, null);
            holder.imvAppIcon = (ImageView)rootView.findViewById(R.id.imvAppIcon);
            holder.imvPadLock = (ImageView)rootView.findViewById(R.id.imvPadlock);
            holder.txvAppName = (TextView)rootView.findViewById(R.id.txvAppName);
            holder.txvAppSystem = (TextView)rootView.findViewById(R.id.txvAppSystem);
            rootView.setTag(holder);

        }else{

            holder = (AppInfoHolder)rootView.getTag();
        }

        holder.imvAppIcon.setImageDrawable(this.localList.get(position).getIcon());
        holder.txvAppName.setText(this.localList.get(position).getAppname());

        if(this.localList.get(position).isBlocked()){

            holder.imvPadLock.setImageResource(R.drawable.padlock_close);

        }else{

            holder.imvPadLock.setImageResource(R.drawable.padlock_open);
        }

        if(this.localList.get(position).isSystemApp()){

            holder.txvAppSystem.setText(R.string.system_app);

        }else{

            holder.txvAppSystem.setText(R.string.non_system_app);
        }

        holder.imvPadLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int pos = position;
                final int POST_DELAYED = 1000;
                Animation aninInvisible = AnimationUtils.loadAnimation(context, R.anim.invisible_image);
                final Animation aninVisible = AnimationUtils.loadAnimation(context, R.anim.visible_image);
                holder.imvPadLock.startAnimation(aninInvisible);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //TODO añadir una linea en la que se busque el objeto al que se la va ha cambiar el estado y el metodo correspondiente
                        AppInfo appInfo = localList.get(pos);
                        appInfo.setBlocked(!appInfo.isBlocked());
                        holder.imvPadLock.setImageResource(appInfo.isBlocked() ? R.drawable.padlock_close:R.drawable.padlock_open);
                        holder.imvPadLock.startAnimation(aninVisible);

                    }
                }, POST_DELAYED);
            }
        });

        return rootView;
    }



    //Instance Methods
    public int orderBy(int typeOrder){

        return 0;
    }

    class AppInfoHolder{

        ImageView imvAppIcon, imvPadLock;
        TextView txvAppName;
        TextView txvAppSystem;
    }
}

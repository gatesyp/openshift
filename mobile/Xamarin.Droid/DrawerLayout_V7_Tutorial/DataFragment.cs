using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using SupportToolbar = Android.Support.V7.Widget.Toolbar;
using SupportFragment = Android.Support.V4.App.Fragment;
using Android.Support.V7.App;
using Android.Support.V4.Widget;
using System.Collections.Generic;

namespace DrawerLayout_V7_Tutorial
{
    class DataFragment : Android.Support.V4.App.Fragment
    {
        public DataFragment()
        {

        }
        public static Android.Support.V4.App.Fragment newInstance(Context context)
        {
            DataFragment busrouteFragment = new DataFragment();
            return busrouteFragment;
        }
        public override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

        }
        public override View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            ViewGroup root = (ViewGroup)inflater.Inflate(Resource.Layout.datafragment, null);
            return root;
        }
    }

}



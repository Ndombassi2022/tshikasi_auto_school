package com.tshikasi.tshikasi_auto_school.utils


sealed class DataState<out T> {
    class SuccessList<T>(val data : List<T>) : DataState<T>()
    class Success<T>(val data : T) : DataState<T>()
    class SuccessAdded<T>(val value : T) : DataState<T>()
    class Failure(val message : String ): DataState<Nothing>()
    class FailureLocalService(val message : String ): DataState<Nothing>()
    data object Start : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Empty : DataState<Nothing>()

}
sealed class DataStateImage<out T> {
    class SuccessList<T>(val data : List<T>) : DataStateImage<T>()
    class Success<T>(val data : T) : DataStateImage<T>()
    class SuccessAdded<T>(val value : T) : DataStateImage<T>()
    class Progress(val progress : Float ): DataStateImage<Float>()
    class Failure(val message : String ): DataStateImage<Nothing>()
    class FailureLocalService(val message : String ): DataStateImage<Nothing>()
    data object Start : DataStateImage<Nothing>()
    data object Loading : DataStateImage<Nothing>()
    data object Empty : DataStateImage<Nothing>()

}

sealed class DataStateDialog<out T> {
    class SuccessList<T>(val data : List<T>) : DataStateDialog<T>()
    class SuccessAdded(val value : String) : DataStateDialog<Nothing>()
    class Failure(val message : String ): DataStateDialog<Nothing>()
    class FailureLocalService(val message : String ): DataStateDialog<Nothing>()
    data object Start : DataStateDialog<Nothing>()
    data object Loading : DataStateDialog<Nothing>()
    data object Empty : DataStateDialog<Nothing>()

}


sealed class DataStateVideoCall {
    class Call(val call : Call?) : DataStateVideoCall()
    class CallState(val callState : CallState?): DataStateVideoCall()
    class Failure(val message : String ): DataStateVideoCall()

    data object Start : DataStateVideoCall()
    data object Loading : DataStateVideoCall()
    data object Empty : DataStateVideoCall()

}

enum class CallState{
    JOINING,
    ACTIVE,
    ENDED
}